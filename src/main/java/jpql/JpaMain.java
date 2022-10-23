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

//            String query = "select m From Member m join fetch m.team t"; 방향을 바꿔서 페이징 해결

            String query = "select t From Team t";

            List<Team> result = em.createQuery(query, Team.class) // <property name="hibernate.default_batch_fetch_size" value="100"/>를 사용해 n+1 문제 해결
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            System.out.println("result = " + result.size());

            for (Team team : result) {
                System.out.println("team = " + team.getName() + "|members=" + team.getMembers().size());
                for (Member member : team.getMembers()) {
                    System.out.println("-> member = " + member);
                }
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
 * 페치 조인의 특성과 한계 (페치 조인은 너무너무 중요하고 페치조인에 대해서는 100프로 이해를 해야함)
 *   * 페치 조인 대상에는 별칭을 줄 수 없다.
 *     * 하이버네이트는 가능, 가급적 사용 X
 *   * 둘 이상의 컬렉션은 페치 조인 할 수 없다.
 *   * 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResult)를 사용할 수 없다.
 *     * 일대일, 다대일 같은 단일 값 관련 연관 필드들은 페치 조인해도 페이징 가능
 *     * 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
 *
 * 페치 조인의 특징과 한계
 *   * 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최적화
 *   * 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
 *     * OneToMany(fetch = FetchType.LAZY) // 글로벌 로딩 전략
 *   * 실무에서 글로벌 로딩 전략은 모두 지연 로딩
 *   * 최적화가 필요한 곳은 페치 조인 적용
 *
 * 페치 조인 - 정리
 *   * 모든 것을 페치 조인으로 해결할 수는 없음
 *   * 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
 *   * 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야하면,
 *     페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO 로 반환 하는 것이 효과적
 /

