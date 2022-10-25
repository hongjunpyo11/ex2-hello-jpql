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

            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member);
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
 * Named 쿼리 - 어노테이션 너무 지져분해져서 좀 안좋다고 봄
 * @Entity
 * @NamedQurey(
 *        name = "Member.findByUsername",
 *        query = "select m from Member m where m.username = :username")
 * public class Member {
 *     ...
 * }
 *
 * List<Member> resultList =
 *   em.createNamedQuery("Member.findByUsername", Member.class)
 *          .setParameter("username", "회원1")
 *          .getResultList();
 *
 * Named 쿼리 - 정적 쿼리
 *   * 미리 정의해서 이름을 부여해두고 사용하는 JQPL
 *   * 정적 쿼리
 *   * 어노테이션, XML에 정의
 *   * 애플리케이션 로딩 시점에 초기화 후 재사용
 *   * 애플리케이션 로딩 시점에 쿼리를 검증
 */