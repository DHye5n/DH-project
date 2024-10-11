package com.example.dhproject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "board",
       indexes = {@Index(name = "board_userid_idx", columnList = "memberid")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE board SET deleted_date = CURRENT_TIMESTAMP WHERE boardid = ?")
@Where(clause = "deleted_date IS NULL")
public class BoardEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public BoardEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
