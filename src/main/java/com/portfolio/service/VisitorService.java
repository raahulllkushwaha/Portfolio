package com.portfolio.service;

import com.portfolio.model.VisitorLog;
import com.portfolio.repository.VisitorLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class VisitorService {

    @Autowired
    private VisitorLogRepository visitorLogRepository;

    public void trackVisit(HttpServletRequest request, String page) {
        String ip = getClientIp(request);

        // Don't track localhost visits
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) return;

        VisitorLog log = VisitorLog.builder()
                .ipAddress(ip)
                .userAgent(request.getHeader("User-Agent"))
                .page(page)
                .build();

        visitorLogRepository.save(log);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        LocalDateTime today     = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime thisWeek  = LocalDateTime.now().minusDays(7);
        LocalDateTime thisMonth = LocalDateTime.now().minusDays(30);

        stats.put("totalVisits",        visitorLogRepository.count());
        stats.put("uniqueVisitors",     visitorLogRepository.countUniqueVisitors());
        stats.put("visitsToday",        visitorLogRepository.countByVisitedAtAfter(today));
        stats.put("visitsThisWeek",     visitorLogRepository.countByVisitedAtAfter(thisWeek));
        stats.put("visitsThisMonth",    visitorLogRepository.countByVisitedAtAfter(thisMonth));
        stats.put("uniqueThisMonth",    visitorLogRepository.countUniqueVisitorsAfter(thisMonth));
        stats.put("recentVisits",       visitorLogRepository.findTop20ByOrderByVisitedAtDesc());

        return stats;
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}