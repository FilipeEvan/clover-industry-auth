package br.com.jacto.cloverindustryauth.repository.product;

import br.com.jacto.cloverindustryauth.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
}
