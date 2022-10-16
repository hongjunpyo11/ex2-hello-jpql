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
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("꽌리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select nullif(m.username, '관리자') as username " +
                    "from Member m ";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
            } // COALESCE

//            String query = "select coalesce(m.username, '이름 없는 회원') as username " +
//                    "from Member m ";
//            List<String> result = em.createQuery(query, String.class)
//                    .getResultList();
//            for (String s : result) {
//                System.out.println("s = " + s);
//            } // COALESCE

//            String query =
//                    "select " +
//                            "case when m.age <= 10 then '학생요금' " +
//                            "     when m.age >= 60 then '경로요금' " +
//                            "     else '일반요금' " +
//                            "end " +
//                    "from Member m";
//            List<String> result = em.createQuery(query, String.class)
//                    .getResultList();
//
//            for (String s : result) {
//                System.out.println("s = " + s);
//            } 기본 CASE 식

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
 * 조건식 - CASE 식
 *   * 기본 CASE 식
 *     * select
 *         case when m.age <= 10 then '학생요금'
 *              when m.age >= 60 then '경로요금'
 *              else '일반요금'
 *         end
 *       from Member m
 *
 *   * 단순 CASE 식
 *     * select
 *         case when '팀A' then '인센티브 110%'
 *              when '팀B' then '인센티브 120%'
 *              else '인센티브 105%'
 *         end
 *       from Team t
 *
 *   * COALESCE: 하나씩 조회해서 null이 아니면 반환
 *     * 사용자 이름이 없으면 이름 없는 회원을 반환
 *       * select coalesce(m.username, '이름 없는 회원') from Member m
 *
 *   * NULLIF: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
 *     * 사용자 이름이 '관리자'면 null 을 반환하고 나머지는 본인의 이름을 반환
 *       * select NULLIF(m.username, '관리자') from Member m
 */


