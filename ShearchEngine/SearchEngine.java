import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine {
    private final Map<String, Integer> searchResults;
    private final Set<String> visitedUrls;
    private final HttpClient http;
    private String searchKeyword;

    public SearchEngine() {
        searchResults = new HashMap<>();
        visitedUrls = new HashSet<>();
        http = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public void searchInWebsite(String url, int depth, String keyword) {
        this.searchKeyword = keyword.toLowerCase();
        searchResults.clear();
        visitedUrls.clear();
        crawlUrl(url, depth);
    }

    private void crawlUrl(String url, int depth) {
        if (visitedUrls.contains(url)) return;
        visitedUrls.add(url);

        String html = fetchHtml(url);
        if (html == null) return;

        // Tìm kiếm từ khóa trong HTML
        int count = countKeywordOccurrences(html, searchKeyword);
        if (count > 0) {
            searchResults.put(url, count);
        }

        if (depth > 0) {
            Set<String> childUrls = getChildUrls(url, html);

            Iterator<String> iterator = childUrls.iterator();
            while (iterator.hasNext()) {
                String childUrl = iterator.next();
                if (visitedUrls.contains(childUrl)) {
                    iterator.remove();
                }
            }
            for (String childUrl : childUrls) {
                crawlUrl(childUrl, depth - 1);
            }
        }
    }

    private int countKeywordOccurrences(String html, String keyword) {
        // Loại bỏ các thẻ HTML để đếm từ khóa trong text thuần
        String textOnly = html.replaceAll("<[^>]+>", " ");
        String lowerText = textOnly.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();
        
        int count = 0;
        int index = 0;
        
        while ((index = lowerText.indexOf(lowerKeyword, index)) != -1) {
            count++;
            System.out.println("Tìm thấy '" + keyword + "' tại vị trí: " + index + 
                             " - Context: ..." + getContext(textOnly, index, keyword.length()) + "...");
            index += lowerKeyword.length();
        }
        
        return count;
    }
    
    private String getContext(String text, int index, int keywordLength) {
        int start = Math.max(0, index - 30);
        int end = Math.min(text.length(), index + keywordLength + 30);
        return text.substring(start, end).replace("\n", " ").replace("\r", " ");
    }

    private String fetchHtml(String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Language", "vi-VN,vi;q=0.9,en-US;q=0.8,en;q=0.7")
                    .GET()
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            int status = res.statusCode();
            if (status == 200) {
                return res.body();
            } else {
                System.out.println("HTTP " + status + " khi tải: " + url);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải URL (" + url + "): " + e.getMessage());
            return null;
        }
    }

    private Set<String> getChildUrls(String parentUrl, String html) {
        Set<String> childUrls = new HashSet<>();
        Pattern pattern = Pattern.compile("<a\\s+[^>]*href\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String childUrl = matcher.group(1).trim();

            if (childUrl.isEmpty()
                    || childUrl.startsWith("#")
                    || childUrl.startsWith("mailto:")
                    || childUrl.startsWith("javascript:")
                    || childUrl.startsWith("tel:")) {
                continue;
            }

            String normalized = normalizeUrl(childUrl, parentUrl);
            if (normalized != null) {
                childUrls.add(normalized);
            }
        }
        return childUrls;
    }

    private String normalizeUrl(String childUrl, String parentUrl) {
        try {
            URL base = new URL(parentUrl);
            URL absolute = new URL(base, childUrl);

            String protocol = absolute.getProtocol();
            if (!protocol.equals("http") && !protocol.equals("https")) return null;

            String path = absolute.getPath();
            if (path == null || path.isEmpty()) path = "/";

            String normalizedUrl = protocol + "://" + absolute.getHost();
            if (absolute.getPort() != -1 && absolute.getPort() != absolute.getDefaultPort()) {
                normalizedUrl += ":" + absolute.getPort();
            }
            normalizedUrl += path;

            if (normalizedUrl.endsWith("/") && !"/".equals(path)) {
                normalizedUrl = normalizedUrl.substring(0, normalizedUrl.length() - 1);
            }
            return normalizedUrl;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public void saveSearchResultsToFile(String filename) {
        // Sử dụng TreeMap với Comparator để sắp xếp theo số lần xuất hiện giảm dần
        TreeMap<String, Integer> sortedResults = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String url1, String url2) {
                int count1 = searchResults.get(url1);
                int count2 = searchResults.get(url2);
                
                // Sắp xếp giảm dần theo số lần xuất hiện
                int compareByCount = Integer.compare(count2, count1);
                if (compareByCount != 0) {
                    return compareByCount;
                }
                // Nếu số lần xuất hiện bằng nhau, sắp xếp theo URL
                return url1.compareTo(url2);
            }
        });
        
        sortedResults.putAll(searchResults);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Từ khóa tìm kiếm: " + searchKeyword + System.lineSeparator());
            writer.write("Tổng số trang tìm thấy: " + sortedResults.size() + System.lineSeparator());
            writer.write("-----------------------------------" + System.lineSeparator());
            
            for (Map.Entry<String, Integer> entry : sortedResults.entrySet()) {
                writer.write(entry.getKey() + " - Số lần xuất hiện: " + entry.getValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    public void displayResults() {
        if (searchResults.isEmpty()) {
            System.out.println("Không tìm thấy kết quả nào!");
            return;
        }

        // Sắp xếp kết quả theo số lần xuất hiện giảm dần
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(searchResults.entrySet());
        sortedList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        System.out.println("\n========== KẾT QUẢ TÌM KIẾM ==========");
        System.out.println("Từ khóa: " + searchKeyword);
        System.out.println("Tổng số trang tìm thấy: " + sortedList.size());
        System.out.println("=====================================");
        
        for (Map.Entry<String, Integer> entry : sortedList) {
            System.out.println(entry.getKey() + " - Số lần xuất hiện: " + entry.getValue());
        }
    }

    private static String getWebsiteName(String websiteUrl) {
        try {
            URL u = new URL(websiteUrl);
            String host = u.getHost();
            String[] parts = host.split("\\.");
            if (parts.length >= 2) return parts[parts.length - 2];
            return host.replaceAll("\\W+", "_");
        } catch (MalformedURLException e) {
            return "site";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SearchEngine searchEngine = new SearchEngine();

        System.out.println("========================================");
        System.out.println("     SEARCH ENGINE - TÌM KIẾM WEB      ");
        System.out.println("========================================");

        // 1. Nhập địa chỉ website và độ sâu
        System.out.print("\n1. Nhập địa chỉ website: ");
        String websiteUrl = scanner.nextLine().trim();

        System.out.print("2. Nhập độ sâu tìm kiếm: ");
        int depth = scanner.nextInt();
        scanner.nextLine();

        // 3. Nhập từ khóa cần tìm
        System.out.print("3. Nhập từ khóa cần tìm kiếm: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Từ khóa không được để trống!");
            return;
        }

        // 4. Thực hiện crawl và tìm kiếm
        String websiteName = getWebsiteName(websiteUrl);
        String outputFileName = websiteName + "_search_results.txt";

        System.out.println("\n4. Đang tìm kiếm từ khóa '" + keyword + "' trong " + websiteUrl + "...");
        System.out.println("   (Độ sâu: " + depth + ")");
        
        searchEngine.searchInWebsite(websiteUrl, depth, keyword);
        
        // 5. Hiển thị và lưu kết quả
        searchEngine.displayResults();
        searchEngine.saveSearchResultsToFile(outputFileName);

        System.out.println("\n5. Kết quả đã được lưu vào file: " + outputFileName);
        System.out.println("========================================");
        
        scanner.close();
    }
}
