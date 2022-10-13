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

            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class) // 파라미터 바인딩 - 이름 기준
                .setParameter("username", "member1")
                .getSingleResult();

            System.out.println("singleResult = " + result.getUsername());


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
 * JPQL 문법
 * select m from Member as m where m.age > 18
 * 엔티티와 속성은 대소문자 구분 O (Member, age)
 * JPQL 키워드는 대소문자 구분 X (SELECT, FROM, where)
 * 엔티티 이름 사용, 테이블 이름이 아님(Member)
 * 별칭은 필수(m) (as는 생략가능)
 * 웬만한 ANSI 구문 사용가능
 *
 * TypeQuery, Query
 *   * TypeQuery: 반환 타입이 명확할 때 사용
 *   * Query: 반환 타입이 명확하지 않을 때 사용
 *
 * 결과 조회 API
 * query.getResultList(): 결과가 하나 이상일 때, 리스트 반환
 *   * 결과가 없으면 빈 리스트 반환
 * query.getSingleResult(): 결과가 정확히 하나, 단일 객체 반환
 *   * 결과가 없으면: javax.persistence.NoResultException
 *   * 둘 이상이면: javax.persistence.NonUniqueResultException
 */


