package com.devsuperior.dsmeta.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.projection.SaleReportProjection;
import com.devsuperior.dsmeta.projection.SaleSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> obj = repository.findById(id);
		Sale entity = obj.orElseThrow(() -> new RuntimeException("Sale not found"));
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportProjection> findReport(
			String minDateStr,
			String maxDateStr,
			String name,
			Pageable pageable) {

		LocalDate today = LocalDate.now();

		LocalDate maxDate = (maxDateStr == null || maxDateStr.isEmpty())
				? today
				: LocalDate.parse(maxDateStr);

		LocalDate minDate = (minDateStr == null || minDateStr.isEmpty())
				? maxDate.minusYears(1)
				: LocalDate.parse(minDateStr);

		name = (name == null) ? "" : name;

		return repository.searchSales(minDate, maxDate, name, pageable);
	}

	public List<SaleSummaryProjection> findSummary(
			String minDateStr,
			String maxDateStr) {

		LocalDate today = LocalDate.now();

		LocalDate maxDate = (maxDateStr == null || maxDateStr.isEmpty())
				? today
				: LocalDate.parse(maxDateStr);

		LocalDate minDate = (minDateStr == null || minDateStr.isEmpty())
				? maxDate.minusYears(1)
				: LocalDate.parse(minDateStr);

		return repository.summarySales(minDate, maxDate);
	}
}
