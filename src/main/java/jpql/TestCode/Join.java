package jpql.TestCode;

import jpql.Member;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Join {

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

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            //1. 내부 조인 (inner 생략 가능)
//            String query = "select m from Member m inner join m.team t";
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();

            //2. 외부 조인 (outer 생략 가능)
//            String query = "select m from Member m left outer join m.team t";
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();

            //3. 세타 조인
//            String query = "select m from Member m, Team t where m.username = t.name";
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();
//
//            System.out.println("result = " + result.size());

            //4. 조인 ON 절 - 조인 대상 필터링
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();

            //5. 조인 ON 절 - 연관관계 없는 엔티티 외부 조인
            String query = "select m from Member m left join Team t on m.username = t.name";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

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
