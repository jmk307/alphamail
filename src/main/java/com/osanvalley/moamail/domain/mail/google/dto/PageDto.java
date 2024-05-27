package com.osanvalley.moamail.domain.mail.google.dto;

import com.osanvalley.moamail.domain.mail.entity.Mail;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageDto {
    private List<MailResponseDto> mails;

    private int pageNumber;

    private int totalPage;

    private int startNumber;

    private int endNumber;

    private boolean hasPrev;

    private boolean hasNext;

    private List<Integer> pageList;

    @Builder
    public PageDto(List<MailResponseDto> mails, int pageNumber, int totalPage, int startNumber, int endNumber, boolean hasPrev, boolean hasNext, List<Integer> pageList) {
        this.mails = mails;
        this.pageNumber = pageNumber;
        this.totalPage = totalPage;
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        this.hasPrev = hasPrev;
        this.hasNext = hasNext;
        this.pageList = pageList;
    }

    public static PageDto of(Page<Mail> mails) {
        List<MailResponseDto> mailResponseDtos = mails.stream()
            .map(MailResponseDto::of)
            .collect(Collectors.toList());

        int pageNumber = mails.getPageable().getPageNumber() + 1;
        int totalPage = mails.getTotalPages();
        int startNumber = (int)((Math.floor((double) pageNumber / 10) * 10) + 1 <= totalPage ? (Math.floor((double) pageNumber / 10) * 10) + 1 : totalPage);
        int endNumber = (Math.min(startNumber + 10 - 1, totalPage));
        boolean hasPrev = mails.hasPrevious();
        boolean hasNext = mails.hasNext();
        List<Integer> pageList = IntStream.rangeClosed(startNumber, endNumber)
                .boxed()
                .collect(Collectors.toList());

        return PageDto.builder()
                .mails(mailResponseDtos)
                .pageNumber(pageNumber)
                .totalPage(totalPage)
                .startNumber(startNumber)
                .endNumber(endNumber)
                .hasPrev(hasPrev)
                .hasNext(hasNext)
                .pageList(pageList)
                .build();

    }
}
