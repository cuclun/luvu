package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Product;
import com.dhsp.luvu.entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// tất cả Entity đều có repository kết thừa thằng ni JpaRepository<tên Entity, Kiểu của id>
// kiểu như: trong cái bảng nớ mình có thể làm cái chi là mình ghi vô repository ni à..
// Chỉ repository mới tương tác được với db...
// Khi gửi request tới servẻ thì nó vô controller controller sẽ gọi tới sẻvice
// Service là nơi xử lí logic
// Service gọi tới repository.. Chỉ repository mới tương tác được với db... tương tác với db để thêm xóa sửa hoặc lấy dữ liệu ra
public interface SpecificationRepository extends JpaRepository<Specification, Long> {
    List<Specification> findAllByProduct(Product product);

    void deleteAllByProduct(Product product);
}
