package jpql.TestCode;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class PathExpression {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //EntityManager를 통해 DB 작업
        EntityManager em = emf.createEntityManager();

        //JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            //컬렉션 값 연관 필드
            //묵시적 내부 조인 (사용하지 말 것)
            String query = "select t.members from Team t";

            List<Collection> result = em.createQuery(query, Collection.class)
                    .getResultList();

            System.out.println("result = " + result);

            //명시적 내부 조인
            String query2 = "select m.username from Team t join t.members m";

            List<String> result2 = em.createQuery(query2, String.class)
                    .getResultList();

            for (String s : result2) {
                System.out.println("s = " + s);
            }

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
