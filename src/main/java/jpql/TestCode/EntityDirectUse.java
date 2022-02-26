package jpql.TestCode;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class EntityDirectUse {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //EntityManager를 통해 DB 작업
        EntityManager em = emf.createEntityManager();

        //JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            em.flush();
            em.clear();

            //1. 엔티티를 파라미터로 전달 (기본 키 값)
            String query = "select m from Member m where m = :member";

            Member findMember = em.createQuery(query, Member.class)
                    .setParameter("member", member2)
                    .getSingleResult();

            System.out.println("findMember = " + findMember);

            //2. 식별자를 직접 전달 (기본 키 값)
            String query2 = "select m from Member m where m.id = :memberId";

            Member findMember2 = em.createQuery(query2, Member.class)
                    .setParameter("memberId", member2.getId())
                    .getSingleResult();

            System.out.println("findMember2 = " + findMember2);

            //3. 외래 키 값
            String query3 = "select m from Member m where m.team = :team";

            List<Member> members = em.createQuery(query3, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

            for (Member member : members) {
                System.out.println("member = " + member);
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
