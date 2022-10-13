package jpql;

import javax.persistence.*;

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

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            Query query3 = em.createQuery("select m.username, m.age from Member m");

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
 */


