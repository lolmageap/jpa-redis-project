package cherhy.soloProject.application.domain.reply.dto.response;

import java.time.LocalDateTime;

public record ReplyRedisDto(Long replyId, LocalDateTime lastModifiedDate) {
    
}
