package integra.security.filter;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Interface for determining if requests should be filtered.
 * Follows ISP - Single responsibility for path matching logic.
 */
public interface FilterPathMatcher {

    /**
     * Determines if the request should skip JWT filtering.
     *
     * @param request HTTP request to evaluate
     * @return true if request should skip filtering, false otherwise
     */
    boolean shouldSkipFiltering(HttpServletRequest request);
}