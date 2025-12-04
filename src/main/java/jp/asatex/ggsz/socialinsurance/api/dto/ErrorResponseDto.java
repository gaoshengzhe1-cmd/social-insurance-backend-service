package jp.asatex.ggsz.socialinsurance.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 全局异常响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    
    /**
     * 错误发生时间戳
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    /**
     * HTTP状态码
     */
    private int status;
    
    /**
     * 错误类型
     */
    private String error;
    
    /**
     * 错误消息
     */
    private String message;
    
    /**
     * 请求路径
     */
    private String path;
    
    /**
     * 创建错误响应DTO的工厂方法
     */
    public static ErrorResponseDto create(HttpStatus status, String error, String message, String path) {
        return ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
}
