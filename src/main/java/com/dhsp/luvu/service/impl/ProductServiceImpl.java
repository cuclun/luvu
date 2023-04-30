package com.dhsp.luvu.service.impl;

import com.dhsp.luvu.dto.request.ProductRequest;
import com.dhsp.luvu.dto.response.ImageResponse;
import com.dhsp.luvu.dto.response.ProductResponse;
import com.dhsp.luvu.dto.response.SpecificationResponse;
import com.dhsp.luvu.entity.Image;
import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.entity.Specification;
import com.dhsp.luvu.repository.CollectionRepository;
import com.dhsp.luvu.repository.ImageRepository;
import com.dhsp.luvu.repository.ProductRepository;
import com.dhsp.luvu.repository.SpecificationRepository;
import com.dhsp.luvu.service.ProductService;
import com.dhsp.luvu.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    CollectionRepository collectionRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    SpecificationRepository specificationRepo;

    @Autowired
    ImageRepository imageRepo;

    @Override
    public ProductResponse findById(Long id) {
        Product product = productRepo.getById(id);
        List<Image> imageList = imageRepo.findAllByProduct(product);
        List<ImageResponse> imageResponses = new ArrayList<>();
        List<Specification> specifications = specificationRepo.findAllByProduct(product);
        List<SpecificationResponse> specificationResponses = new ArrayList<>();
        List<String> sizeResponses = new ArrayList<>();
        List<String> colorResponses = new ArrayList<>();

        for (Image image : imageList) {
            ImageResponse imageResponse = new ImageResponse(image.getId(), image.getName());
            imageResponses.add(imageResponse);
        }

        for (Specification specification : specifications) {
            String content = specification.getContent();
            SpecificationResponse specificationResponse = new SpecificationResponse(specification.getId(), content);
            specificationResponses.add(specificationResponse);
            if (content.contains("Size")) {
                String sizes = content.substring(5);
                sizeResponses = Arrays.asList(sizes.split("-"));
            }

            if (content.contains("Màu")) {
                String colors = content.substring(4);
                colorResponses = Arrays.asList(colors.split(","));
            }
        }

        return new ProductResponse(
                product.getId(),
                product.getName().toUpperCase(),
                product.getPrice(),
                product.getQuantity(),
                product.getDescription(),
                imageResponses,
                specificationResponses,
                product.getCollection().getId(),
                sizeResponses,
                colorResponses);
    }

    @Override
    public List<ProductResponse> search(String keyword) {
        List<Product> products = productRepo.findAllByNameContaining(keyword);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse productResponse = findById(product.getId());
            productResponses.add(productResponse);
        }
        return productResponses;
    }

    @Override
    public List<ProductResponse> findByCollectionId(Long id) {
        List<Product> products = productRepo.findByCollection_Id(id);
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse productResponse = findById(product.getId());
            productResponses.add(productResponse);
        }
        return productResponses;
    }

    @Override
    @Transactional
    public ProductResponse save(ProductRequest request) {
        if (productRepo.existsByName(request.getName()))
            throw new RuntimeException("Sản phẩm đã tồn tại trong hệ thống!");

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
        product.setCollection(collectionRepo.findById(request.getCollectionId()).orElseThrow(() -> new RuntimeException("Danh mục không tồn tại!")));

        try {
            validate(request);// phải bỏ trong try mới bắt ngoại lệ của hàm đó được.
            product = productRepo.save(product);
            for (MultipartFile image : request.getImages()) {
                String nameImage = UploadUtils.save(image);
                imageRepo.save(new Image(nameImage, product));
            }

            for (int i = 1; i < request.getSpecifications().length; i++) {
                specificationRepo.save(new Specification(request.getSpecifications()[i], product));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return findById(product.getId());
    }

    //kệ hắn, rứa được rồi đó tiếp cái try catch đi.. tới đâu rồi he :v
    @Override
    @Transactional // hiểu cái transactional ni không như trigger rứa.. có giống k he :v
    //trigger thêm sửa xóa mà không hợp lí cái sài rollbaack là hắn rollback đó..
    // chỗ ni khác xíu
    // Ví dụ chỉ select không thay đổi dữ liệu thì không cần.
    // Khi có @Transactional nó sẽ chạy hết hàm rồi mới commit... ví dụ..
    public ProductResponse update(ProductRequest request, Long id) {

        // Tìm kiếm sản phẩm muốn chỉnh sửa thông tin.
        // Nếu tìm không thầy sản phẩm sẽ ném về 1 ngoại lệ RuntimeException có message. khi ném về ngoại rồi thì giống như return không ththwucj hiện câu lệnh ở dưới.
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Sản phẩm này không tồn tại!"));
        try {
            //xong ở đây
            validate(request);// nếu nó không lỗi thì sẽ lưu hoặc update nếu
            // Xóa tất cả specification hiện tại của product. là xóa cái chi
            specificationRepo.deleteAllByProduct(product); // ý là dùng lại của họ.. Jpa có sẵn rất nhiêu hàm cơ bản.
            // Nếu không có hàm mình cần. Chỉ cần đặt tên hàm theo quy tắc thì Jpa tự hiểu câu lệnh Query...
            // ời hỏi chữ phía trước dấu ., phía sau chưa hỏi. đọc thi

            // Set các thuộc tính product lại
            product.setName(request.getName()); // chừ mình muốn nếu họ nhập tên dài quá thì nói là nhập ngắn thôi
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setDescription(request.getDescription());
            // Giống như sản phẩm ở trên... Tìm kiếm danh mục...
            product.setCollection(collectionRepo.findById(request.getCollectionId()).orElseThrow(() -> new RuntimeException("Danh mục không tồn tại!")));

            // lưu sản phẩm... Cũng là hàm save nhuưng nếu có id thì sẽ chỉnh sửa sản phẩm theo id đó. nếu không có thì sẽ tạo mới.
            productRepo.save(product);// ở đây lưu sản phẩm xong
// ví dụ specification cũ của sản phẩm 1 2 3. bấm chỉnh sửa nhưng không thay đổi... nó vẫn xóa 1 2 3 rồi lưu 1 2 3 lại.
            // lưu specification lại cái nớ có sẵn à
            // cái mã đang hiển thị đó là cái cũ. mình lấy ra từ db. xong sửa hay không chỉnh sửa gì. nó cũng đem cái mảng đó gửi vô lại để lưu.
            for (int i = 1; i < request.getSpecifications().length; i++) {
                specificationRepo.save(new Specification(request.getSpecifications()[i], product));
            }
            // chừ mình muốn kiểm tra cái chi là mình if từng cái rứa à.. nếu có cách mô hay hơn thì làm :v ok ok rồi... nếu kiểu tra ở đây thì khi tạo 1 product mới cũng phải kiểm tra
            // thì sẽ tạo ra 1 cái gọi là validate...
            return findById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Không thể chỉnh sửa thành sản phẩm đã có trong hệ thống!");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Sản phẩm này không tồn tại!"));
        try {
            specificationRepo.deleteAllByProduct(product);
            imageRepo.deleteAllByProduct(product);
            productRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    // là làm cái chung ni, xong mô muốn sài là ghi à đúng. mà chỉ 2 cái là cần thôi save với update. rứa nảy m ghi boolean thì làm răng
    // tính ghi boolean nhưng chỉ trả về đúng sai
    // không biết lỗi chi nên sửa
    // để boolean vẫn throw được. nhưng mà thừa nên sửa thành void
    boolean validate(ProductRequest product) {
        if(product.getName().length() > 255)
            throw new RuntimeException("Nhập ngắn thôi má");
        return true;
    }
}
