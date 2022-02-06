package com.example.springbatch.project.batch.chunk.writer;

import com.example.springbatch.project.batch.domain.ApiRequestVO;
import com.example.springbatch.project.batch.domain.ApiResponseVO;
import com.example.springbatch.project.service.AbstractApiService;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

public class ApiItemWriter3 extends FlatFileItemWriter<ApiRequestVO> {

    private final AbstractApiService apiService;

    public ApiItemWriter3(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {
        ApiResponseVO responseVO = apiService.service(items);
        System.out.println("responseVO = " + responseVO);

        // item마다 저장
        items.forEach(item -> item.setApiResponseVO(responseVO));

        super.setResource(new FileSystemResource("C:\\Programer\\spring-study\\spring-batch\\src\\main\\resources\\product3.txt"));
        super.open(new ExecutionContext());
        super.setLineAggregator(new DelimitedLineAggregator<>()); // 구분자방식
        super.setAppendAllowed(true); // 계속 추가하는 기능
        super.write(items); // 부모에게 items를 넘거줌
    }
}
