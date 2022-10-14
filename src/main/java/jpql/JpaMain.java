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
            member.setUsername("teamA");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m from Member m left join Team t on m.username = t.name";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            System.out.println("result.size() = " + result.size());

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
 * 조인
 * * 내부 조인: SELECT m FROM Member m [INER] JOIN m.team t
 * * 외부 조인: SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
 * * 세타 조인: select count(m) from Member m, Team t where m.username = t.name
 *
 * 조인 ON 절(JPA 2.1부터 지원)
 * 1. 조인 대상 필터링
 * 2. 연관관계 없는 엔티티 외부 조인(하이버네이트 5.1부터)
 *
 * 1. 조인 대상 필터링 예시
 * 예) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인
 * JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'
 * SQL:  SELECT m*,t.*FROM
 *       Member m LEFT JOIN Team t ON m.TEAM_ID=t.id and t.name='A'
 *
 * 2. 연관관계 없는 엔티티 외부 조인 예시
 * 예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인
 * JPQL: SELECT m, t FROM
 *       Member m LEFT JOIN Team t on m.username = t.name
 * SQL:  SELECT m*,t.*FROM
 *       Member m LEFT JOIN Team t ON m.username = t.name
 *
 */


