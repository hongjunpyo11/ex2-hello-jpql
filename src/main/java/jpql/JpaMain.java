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

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamA.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);


            em.flush();
            em.clear();

            String query = "select t From Team t join fetch t.members";

            List<Team> result = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : result) {
                System.out.println("team = " + team.getName() + ", " + team.getMembers().size());
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
 * 페치 조인(fetch join)
 *   * SQL 조인 종류 X
 *   * JPQL에서 성능 최적화를 위해 제공하는 기능
 *   * 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
 *   * join fetch 명령어 사용
 *   * 페치 조인 ::= [LEFT [OUTER] | INNER] JOIN FETCH 조인경로
 *
 * 엔티티 페치 조인
 *   * 회원을 조회하면서 연관된 팀도 함께 조회(SQL 한 번에)
 *   * SQL을 보면 회원 뿐만 아니라 팀(T.*)도 함께 SELECT
 *   * [JPQL]
 *     select m from Member m join fetch m.team
 *   * [SQL]
 *     SELECT M.*, T.* FROM MEMBER M
 *     INNER JOIN TEAM T ON M.TEAM_ID = T.ID
 *
 * 컬렉션 페치 조인
 *   * 일대다 관계, 컬렉션 페치 조인
 *
 *   * [JPQL]
 *     select t
 *     from Team t join fetch t.members
 *     where t.name = '팀A'
 *
 *   * [SQL]
 *     SELECT T.*, M.*
 *     FROM TEAM T
 *     INNER JOIN MEMBER M ON T.ID = M.TEAM_ID
 *     WHERE T.NAME = '팀A'
 */


