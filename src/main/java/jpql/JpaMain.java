package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //EntityManager를 통해 DB 작업
        EntityManager em = emf.createEntityManager();

        //JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            //1. TypeQuery (반환 타입이 명확), Query (반환 타입이 명확 x)
//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//            Query query3 = em.createQuery("select m.username, m.age from Member m");

            //2. 결과 조희 API
//            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
//
//            //결과가 하나 이상 -> 리스트 반환
//            //결과가 없으면 빈 리스트 반환
//            List<Member> resultList = query.getResultList();
//            for (Member member1 : resultList) {
//                System.out.println("member1 = " + member1);
//            }
//
//            TypedQuery<Member> query = em.createQuery("select m from Member m where m.id = 10", Member.class);
//
//            //결과가 정확히 하나 -> 단일 객체 반환
//            //결과가 없으면 NoResultException, 둘 이상이면 NonUniqueResultException
//            Member result = query.getSingleResult();
//            System.out.println("result = " + result);

            //3. 파라미터 바인딩
            //이름 기준
            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("result = " + result.getUsername());

            //위치 기준 (사용 x)
            Member result2 = em.createQuery("select m from Member m where m.username = ?1", Member.class)
                    .setParameter(1, "member1")
                    .getSingleResult();
            System.out.println("result2 = " + result2.getUsername());

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