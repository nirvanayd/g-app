package com.nelly.application.service;

import com.nelly.application.domain.SearchLog;
import com.nelly.application.domain.Users;
import com.nelly.application.enums.SearchLogType;
import com.nelly.application.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SearchDomainService {
    private final SearchLogRepository searchLogRepository;

    public void saveLog(Optional<Users> user, SearchLogType searchLogType, String keyword) {

        if (user.isPresent()) {
            Optional<SearchLog> existLog = searchLogRepository.findByUserIdAndKeyword(user.get().getId(), keyword);
            existLog.ifPresent(searchLogRepository::delete);
        }

        SearchLog searchLog = SearchLog.builder().keyword(keyword).searchLogType(searchLogType)
                .userId(user.map(Users::getId).orElse(null)).build();
        searchLogRepository.save(searchLog);
    }

    public Page<SearchLog> selectUserSearchLog(Users user) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        return searchLogRepository.findAllByUserId(user.getId(), pageRequest);
    }

    public void deleteUserCurrentKeyword(Users user, String keyword) {
        Optional<SearchLog> existLog = searchLogRepository.findByUserIdAndKeyword(user.getId(), keyword);
        if (existLog.isPresent()) {
            searchLogRepository.delete(existLog.get());
        }
    }
}
