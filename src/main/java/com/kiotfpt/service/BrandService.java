package com.kiotfpt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kiotfpt.model.Brand;
import com.kiotfpt.model.ResponseObject;
import com.kiotfpt.repository.BrandRepository;
import com.kiotfpt.repository.ProductRepository;
import com.kiotfpt.response.BrandResponse;

@Service
public class BrandService {

	@Autowired
	private BrandRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
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
        List<Brand> popularBrands = repository.findPopularBrandsWithLimit(PageRequest.of(0, 4));

        if (popularBrands.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(false, HttpStatus.NOT_FOUND.toString().split(" ")[0],
                            "No popular brands found.", null));
        }

        List<BrandResponse> brandResponses = popularBrands.stream()
                .map(brand -> new BrandResponse(brand, brand.getProducts()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(true, HttpStatus.OK.toString().split(" ")[0],
                        "Popular brands found.", brandResponses));
    }
}
