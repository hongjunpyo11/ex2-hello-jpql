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

            Member member1 = new Member();
            member1.setUsername("관리자1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);



            em.flush();
            em.clear();

            String query = "select function('group_concat', m.username) From Member m";

            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
            }

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
 * JPQL 기본 함수
 *   * CONCAT
 *   * SUBSTRING
 *   * TRIM
 *   * LOWER, UPER
 *   * LENGTH
 *   * LOCATE
 *   * ABS, SQRT, MOD
 *   * SIZE, INDEX(JPA 용도)
 *
 * 사용자 정의 함수 호출
 *   * 하이버네이트는 사용전 방언에 추가해야 한다.
 *     * 사용하는 DB방언을 상속받고, 사용자 정의 함수를 등록한다.
 *     select function('group_concat', i.name) from Item i
 */


