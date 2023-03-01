package cherhy.soloProject.domain.reply.dto.response;

import java.time.LocalDateTime;

public record ReplyRedisDto(Long replyId, LocalDateTime lastModifiedDate) {
    
}
