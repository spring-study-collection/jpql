package jpql.TestCode;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class BulkOperation {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //EntityManager를 통해 DB 작업
        EntityManager em = emf.createEntityManager();

        //JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {;

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(0);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(0);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(0);
            member3.setTeam(teamB);
            em.persist(member3);

            //1. 모든 회원의 나이를 20살로 바꾸기
            //flush 자동 호출
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate(); //영향받은 엔티티 수 반환

            System.out.println("resultCount = " + resultCount);

            //영속성 컨텍스트에 나이 = 20 반영 x
            Member findMember = em.find(Member.class, member1.getId());

            System.out.println("findMember = " + findMember.getAge());

            //벌크 연산 수행 후 영속성 컨텍스트 초기화
            em.clear();

            Member findMember2 = em.find(Member.class, member1.getId());

            System.out.println("findMember2 = " + findMember2.getAge());

            //DB에 SQL쿼리를 보내고 커밋
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
