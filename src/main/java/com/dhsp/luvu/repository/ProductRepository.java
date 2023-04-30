package com.dhsp.luvu.repository;

import com.dhsp.luvu.entity.Collection;
import com.dhsp.luvu.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCollection(Collection collection);

    Boolean existsByName(String name);//ghi như ri là hắn tự hiểu kiểm tra sự tồn tại của sản phẩm có cái tên nớ.. Đúng rồi đó. Jpa

    List<Product> findAllByNameContaining(String name);

    List<Product> findByCollection_Id(Long id);

    // ví dụ: tìm kiếm sản phẩm theo số lượng. Có gợi ý hết luôn
    List<Product> findByQuantity(int quantity);// mà hắn không liên quan tới cái t hỏi nói
    // hắn như cái hồi lâu trên cty học hả... JDBC?? thay vì Dùng JDBC... tự viết câu lệnh query thì Jpa hỗ trợ hết rồi nên dùng thôi ke
}
