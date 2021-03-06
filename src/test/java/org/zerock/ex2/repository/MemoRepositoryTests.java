package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;


    @Test
    public void testClass() {
        System.out.println("테스트임" +memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect(){
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("============");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println("여기테스트 " + memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);
        System.out.println("============");
        System.out.println("여기테스트2" + memo);

    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println("등록 " + memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }
    
    @Test
    public void testPageDefault() { // 페이지 처리
        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println("페이지처리: " +result);
    }

    @Test
    public void testPageOrderBy() {
        Sort sort = Sort.by("mno").descending();
        Sort sort1 = Sort.by("memoText").ascending();
        Sort sortAll = sort.and(sort1);

//        Pageable pageable = PageRequest.of(0,10,sort);
        Pageable pageable = PageRequest.of(0,10,sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> System.out.println("내림차순 " + memo));
    }

    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);

        for(Memo memo : list){
            System.out.println("쿼리메소드 : "+ memo);
        }
    }

    @Test
    public void testQueryPageMethods() {
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L,pageable);
        result.get().forEach(memo -> System.out.println("쿼레페이지메소드 " + memo));
    }

    @Commit //deleteBy인경우 select문으로 가져오기때문에 필수
    @Transactional //deleteBy인경우 select문으로 가져오기때문에 필수
    @Test
    public void testDeleteQueryMethods(){
        memoRepository.deleteMemoByMnoLessThan(10L); // 10보다 작은 데이터 삭제
    }

}
