package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.projection.SaleReportProjection;
import com.devsuperior.dsmeta.projection.SaleSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(nativeQuery = true, value = """
    SELECT tb_sales.id, tb_sales.date, tb_sales.amount, tb_seller.name AS sellerName
    FROM tb_sales
    JOIN tb_seller ON tb_sales.seller_id = tb_seller.id
    WHERE tb_sales.date BETWEEN :minDate AND :maxDate
    AND LOWER(tb_seller.name) LIKE LOWER(CONCAT('%', :name, '%'))
""",
            countQuery = """
    SELECT COUNT(*)
    FROM tb_sales
    JOIN tb_seller ON tb_sales.seller_id = tb_seller.id
    WHERE tb_sales.date BETWEEN :minDate AND :maxDate
    AND LOWER(tb_seller.name) LIKE LOWER(CONCAT('%', :name, '%'))
""")
    Page<SaleReportProjection> searchSales(
            LocalDate minDate,
            LocalDate maxDate,
            String name,
            Pageable pageable
    );

    @Query(nativeQuery = true, value = """
    SELECT tb_seller.name AS sellerName, SUM(tb_sales.amount) AS total
    FROM tb_sales
    JOIN tb_seller ON tb_sales.seller_id = tb_seller.id
    WHERE tb_sales.date BETWEEN :minDate AND :maxDate
    GROUP BY tb_seller.name
""")
    List<SaleSummaryProjection> summarySales(
            LocalDate minDate,
            LocalDate maxDate
    );

}
