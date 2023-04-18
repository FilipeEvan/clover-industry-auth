package br.com.jacto.cloverindustryauth.service.product;

import br.com.jacto.cloverindustryauth.ValidationException;
import br.com.jacto.cloverindustryauth.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductUpdateRequestDto;
import br.com.jacto.cloverindustryauth.model.product.Product;
import br.com.jacto.cloverindustryauth.repository.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ProductDetailResponseDto postProduct(ProductCreateRequestDto dto) {
        Product product = new Product(dto);
        product.setCreated_at(LocalDateTime.now(ZoneId.of("UTC"))); // Salva a data de criação do produto
        // Salva as informações do produto no banco de dados
        productRepository.save(product);

        return new ProductDetailResponseDto(product);
    }

    public Page<ProductListResponseDto> getAllProducts(@PageableDefault(page = 0, size = 10, sort = {"name"}) Pageable pageable) {
        var page = productRepository.findAll(pageable).map(ProductListResponseDto::new);
        return page;
    }

    public ProductDetailResponseDto getProductById(UUID id) {
        // Busca o produto correspondente pelo ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Produto não encontrado"));

        return new ProductDetailResponseDto(product);
    }

    @Transactional
    public ProductDetailResponseDto putProductById(UUID id, ProductUpdateRequestDto dto) {
        // Busca o produto correspondente pelo ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Produto não encontrado"));

        // Atualiza as informações do produto
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getPrice() > 0) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getCategory() != null) {
            product.setCategory(dto.getCategory());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            product.setStatus(dto.getStatus());
        }

        return new ProductDetailResponseDto(product);
    }

    @Transactional
    public void deleteProductById(UUID id) {
        // Busca o produto correspondente pelo ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Produto não encontrado"));

        // Exclui as informações do produto do banco de dados
        productRepository.delete(product);
    }
}
