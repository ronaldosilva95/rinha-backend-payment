package br.com.rinha.backend.payment.infra.repository;

import br.com.rinha.backend.payment.infra.repository.model.Metric;
import feign.Param;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricsRepository extends JpaRepository<Metric, String> {

  @Transactional
  @Modifying
  @Query("FROM Metric e WHERE e.requestedAt BETWEEN :startDate AND :endDate AND e.processor = :processor")
  List<Metric> findByRequestedAtBetween(
      @Param("startDate") String startDate,
      @Param("endDate") String endDate,
      @Param("processor") String processor);

  @Query("select COUNT(1), SUM(e.amount), e.processor FROM Metric e WHERE e.requestedAt BETWEEN :startDate AND :endDate GROUP BY e.processor ORDER BY e.processor")
  List<Object[]> findMetricsGroupByProcessorAndRequestedAtBetween(
      @Param("startDate") ZonedDateTime startDate,
      @Param("endDate") ZonedDateTime endDate);

}
