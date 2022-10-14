package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();

            MemberDTO memberDTO = result.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}

/**
 * 프로젝션
 *   * SELECT 절에 조회할 대상을 지정하는 것
 *   * 프로젝션 대상: 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자등 기본 데이터 타입)
 *   * SELECT m.FROM Member m -> 엔티티 프로젝션
 *   * SELECT m.team FROM Member m -> 엔티티 프로젝션
 *   * SELECT m.address FROM Member m -> 임베디드 타입 프로젝션
 *   * SELECT m.username, m.age FROM Member m -> 스칼라 타입 프로젝션
 *   * DISTINCT로 중복 제거
 */


