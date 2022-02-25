package jpql.TestCode;

import jpql.Member;
import jpql.MemberDTO;
import jpql.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Projection {

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

            em.flush();
            em.clear();

            //영속성 컨텍스트에서 관리됨 : 엔티티 프로젝션
            //1. 엔티티 프로젝션 : 영속성 컨텍스트에서 관리됨
//            List<Member> result = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();
//
//            Member findMember = result.get(0);
//            findMember.setAge(20);
//
//            List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
//                    .getResultList();

            //2. 임베디드 타입 프로젝션
//            em.createQuery("select o.address from Order o", Address.class)
//                    .getResultList();

            //3. 스칼라 타입 프로젝션
            //a) 여러 값 조회시 Object[] 타입으로 조회
//            List resultList = em.createQuery("select m.username, m.age from Member m")
//                    .getResultList();
//
//            Object o = resultList.get(0);
//            Object[] result = (Object[]) o;
//            System.out.println("username = " + result[0]);
//            System.out.println("age = " + result[1]);

            //b) 여러 값 조회시 TypeQuery로 조회
//             List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
//                    .getResultList();
//
//             Object[] result = resultList.get(0);
//             System.out.println("username = " + result[0]);
//             System.out.println("age = " + result[1]);

            //c) 여러 값 조회시 new 명령어로 조회
            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());

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
