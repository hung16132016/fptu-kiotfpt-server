package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Brand;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.model.Status;
import com.kiotfpt.repository.BrandRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.repository.StatusRepository;
import com.kiotfpt.request.BrandRequest;
import com.kiotfpt.response.BrandResponse;

@Service
public class BrandService {

	@Autowired
	private BrandRepository repository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StatusRepository statusRepository;

	public ResponseEntity<ResponseObject> getBrandByCategoryID(int category_id) {
		List<Object[]> brandIDList = productRepository.findBrandByCategory(category_id);
		List<Brand> brands = new ArrayList<>();

		for (Object[] obj : brandIDList) {
			Optional<Brand> brand = repository.findById(Integer.parseInt(obj[0].toString()));
			brands.add(brand.get());
		}

		return !brands.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has found successfully", brands))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not found", new int[0]));
	}

	public ResponseEntity<ResponseObject> getPopularBrands() {
		List<Object[]> popularBrands = productRepository.findTop4PopularBrand();
		List<Brand> brands = new ArrayList<>();

		for (Object[] obj : popularBrands) {
			Optional<Brand> brand = repository.findById(Integer.parseInt(obj[0].toString()));
			brands.add(brand.get());
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Get popular brand successfull", brands));
	}

	public ResponseEntity<ResponseObject> getAllBrands() {
		List<Brand> brands = repository.findAll();
		List<BrandResponse> brandResponses = new ArrayList<>();

		for (Brand brand : brands) {
			BrandResponse brandResponse = new BrandResponse(brand);
			brandResponses.add(brandResponse);
		}

		return !brandResponses.isEmpty()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
								"Data has been found successfully", brandResponses))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(false,
						HttpStatus.NOT_FOUND.toString().split(" ")[0], "Data has not been found", new int[0]));
	}

	public ResponseEntity<ResponseObject> getBrandById(int id) {
		Optional<Brand> brand = repository.findById(id);
		if (brand.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
					HttpStatus.OK.toString().split(" ")[0], "Brand fetched successfully", brand.get()));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0], "Brand not found", null));
		}
	}

	public ResponseEntity<ResponseObject> createBrand(BrandRequest brandRequest) {
		Status activeStatus = statusRepository.findByValue("active")
				.orElseThrow(() -> new RuntimeException("Active status not found"));
		Brand brand = new Brand();
		brand.setName(brandRequest.getName());
		brand.setThumbnail(brandRequest.getThumbnail());
		brand.setStatus(activeStatus);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(true,
				HttpStatus.CREATED.toString().split(" ")[0], "Brand created successfully", repository.save(brand)));
	}

	public ResponseEntity<ResponseObject> updateBrand(int id, BrandRequest brandRequest) {
		Brand brand = repository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
		brand.setName(brandRequest.getName());
		brand.setThumbnail(brandRequest.getThumbnail());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(true,
				HttpStatus.OK.toString().split(" ")[0], "Brand updated successfully", repository.save(brand)));
	}

	public ResponseEntity<ResponseObject> deleteBrand(int id) {
		Brand brand = repository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
		Status inactiveStatus = statusRepository.findByValue("inactive")
				.orElseThrow(() -> new RuntimeException("Inactive status not found"));
		brand.setStatus(inactiveStatus);
		repository.save(brand);
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0], "Brand deleted successfully", null));
	}
}
