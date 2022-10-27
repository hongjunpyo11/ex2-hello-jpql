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
            member1.setAge(0);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(0);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(0);
            member3.setTeam(teamB);
            em.persist(member3);

            // FLUSH 자동 호출
           int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

           em.clear();

            Member findMember = em.find(Member.class, member1.getId());

            System.out.println("findMember = " + findMember.getAge());

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
 * 벌크연산
 *   * 재고가 10개 미만인 모든 상품의 가격을 10% 상승하려면?
 *   * JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL 실행
 *     * 재고가 10개 미만인 상품 리스트로 조회한다.
 *     * 상품 엔티티의 가격을 10% 증가한다.
 *     * 트랜잭션 커밋 시점에 변경감지가 동작한다.
 *   * 변경된 데이터가 100건이라면 100번의 UPDATE SQL 실행
 *
 * 벌크 연산 예제
 *   * 쿼리 한번으로 여러 테이블 로우 변경(엔티티)
 *   * executeUpdate()의 결과는 영향받은 엔티티 수 반환
 *   * UPDATE, DELETE 지원
 *   * INSERT(insert into .. select, 하이버네이트 지원원)
 *
 * 벌크 연산 주의
 *   * 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
 *     * 벌크 연산을 먼저 실행
 *     * 벌크 연산 수행 후 영속성 컨텍스트 초기화화
* */