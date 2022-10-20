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

            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);


            em.flush();
            em.clear();

            String query = "select m.username From Team t join t.members m"; // 이 방법이 실무에서 추천됨 명시적 조인을 해야함

            Integer result = em.createQuery(query, Integer.class)
                    .getSingleResult();

            System.out.println("result = " + result);

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
 * 경로 표현식
 *   * .을 찍어서 객체 그래프를 탐색하는 것
 *
 * 경로 표현식 용어 정리
 *   * 상태필드(state field): 단순히 값을 저장하기 위한 필드
 *                          (ex: m.username)
 *   * 연관 필드(association field): 연관관계를 위한 필드
 *     * 단일 값 연관 필드:
 *     @ManyToOne, @OneToOne, 대상이 엔티티 (ex: m.team)
 *     * 컬렉션 값 연관 필드:
 *     @OneToMany, @ManyToMany, 대상이 컬렉션(ex: m.orders)
 *
 * 경로 표현식 특징
 *   * 상태필드(state field): 경로 탐색의 끝, 탐색 X
 *   * 단일 값 연관 필드:묵시적 내부 조인(inner join) 발생, 탐색 O
 *   * 컬렉션 값 연관 필드: 묵시적 내부 조인 발생, 탐색 X
 *     * FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
 *
 *       * 묵시적 조인: 경로 표현식에 의해 묵시적으로 SQL 조인 발생(내부 조인만 가능)
 *         * select m.team from Member m
 */


