package kr.ac.kopo.oracledb0314.repository;

import kr.ac.kopo.oracledb0314.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }
    //MemoRepository의 save(memo entity 객체의 참조값)를 호출해서 insert를 한다.

    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1,100).forEach(i ->{
            Memo memo = Memo.builder().memoText("Dummy Data Test" + i).build();
            memoRepository.save(memo);
        });
    }

    //MemoRepository의 findBuId(Memo Entity 객체의 Id로 설정된 필드값)를 호출해서 select한다.
    //findBuId()호출되면 바로 select문을 실행한다.
    //findBuId()는 NullPointerEcception이 발생되지 않도록 Null 체크를 한다.
    @Test
    public void testSelect(){
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("====================================");

        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    //MemoRepository의 getOne(Memo Entity 객체의 Id로 설정된 필드값)를 호출해서 select한다.
    //getOne()호출되면 바로 실행되지 않고 Memo Entity가 필요할 때 select를 실행한다.
    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("====================================");

        System.out.println(memo);
        }


    //MemoRepository의 save(memo entity 객체의 참조값)를 호출해서 update를 한다.
    //save()는 호충하면 먼저 select를 하기 때문에 기존에 entity 가 있을 때는  update를 실행한다.
    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(95L).memoText("=Update Dummy Data 95").build();

        Memo memo1 = memoRepository.save(memo);

        System.out.println(memo1);
    }


    //MemoRepository의 deleteByid(MemoEntity의 mno 값)를 호출해서 delete 한다.
    @Test
    public void testDelete(){
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){
    //1페이지당 10개의 Entity
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        for(Memo memo : result.getContent()){
            System.out.println(memo);
        }

        System.out.println("------------------------");
        System.out.println("Total Pages: " + result.getTotalPages());
        System.out.println("Total Count: " + result.getTotalElements());
        System.out.println("Page Number: " + result.getNumber());
        System.out.println("Page Size: " + result.getSize());
        System.out.println("has next page?: " + result.hasNext());
        System.out.println("first page?: " + result.isFirst());



    }
    @Test
    public void testSort(){
//        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo.getMno() + ", content:" + memo.getMemoText());
        });
    }

    @Test
    public void testQueryMethod1(){
        List<Memo> result = memoRepository.findByMnoBetweenOrderByMnoDesc(20L,30L);

        for (Memo memo : result){
            System.out.println(memo.toString());
        }
    }

    public void testQueryMethod2(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(20L, 60L, pageable);

        for (Memo memo : result){
            System.out.println(memo.toString());
        }
        System.out.println("-------------------------");

        pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        result = memoRepository.findByMnoBetween(20L, 60L, pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }
    @Commit
    @Transactional
    @Test
    public void testQueryMethod3(){
        memoRepository.deleteMemoByMnoLessThan(5L);
        testPageDefault();
    }

    @Test
    public void testQueryAnnotationNative(){
        List<Memo> result = memoRepository.getNativeResult();
        for (Memo memo : result){
            System.out.println(memo);
        }
    }
    @Test
    public void testQueryAnnotationNative2(){
        List<Object[]> result = memoRepository.getNativeResult2();
        for (Object[] memoObj : result){
            System.out.println(memoObj[1]);
        }
    }


}


