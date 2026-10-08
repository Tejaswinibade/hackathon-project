package com.tejaswini;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationController {

    private static final String HOME_PAGE = """
        <!doctype html>
        <html lang=\"en\">
        <head>
            <meta charset=\"utf-8\">
            <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">
            <title>🔥 FireSmartTV - AI Recommendations</title>
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body {
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
                    background: linear-gradient(135deg, #0f172a 0%, #111827 50%, #1f2937 100%);
                    color: #f3f4f6;
                    min-height: 100vh;
                    padding: 20px;
                }
                .header { text-align: center; margin-bottom: 40px; padding-top: 20px; }
                .header h1 {
                    font-size: 3rem;
                    background: linear-gradient(135deg, #fbbf24, #f59e0b);
                    -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent;
                    background-clip: text;
                    font-weight: 900;
                    letter-spacing: 2px;
                    margin-bottom: 10px;
                }
                .header p { font-size: 1.1rem; color: #d1d5db; font-weight: 300; }
                .container {
                    max-width: 1000px;
                    margin: auto;
                    display: grid;
                    grid-template-columns: 1fr 1.2fr;
                    gap: 30px;
                }
                .form-section, .results-section {
                    background: rgba(17, 24, 39, 0.7);
                    backdrop-filter: blur(10px);
                    border-radius: 20px;
                    padding: 30px;
                    border: 1px solid rgba(75, 85, 99, 0.3);
                }
                .form-section h2 {
                    font-size: 1.5rem;
                    margin-bottom: 20px;
                    color: #fbbf24;
                    font-weight: 600;
                }
                .form-group { margin-bottom: 20px; }
                .form-group label {
                    display: block;
                    font-weight: 600;
                    margin-bottom: 8px;
                    font-size: 0.95rem;
                    color: #e5e7eb;
                    text-transform: uppercase;
                    letter-spacing: 0.5px;
                }
                .form-group select, .form-group textarea {
                    width: 100%;
                    padding: 12px 14px;
                    border-radius: 12px;
                    border: 2px solid rgba(75, 85, 99, 0.4);
                    background: rgba(31, 41, 55, 0.8);
                    color: #f9fafb;
                    font-size: 0.95rem;
                    font-family: inherit;
                    transition: all 0.3s ease;
                }
                .form-group select:focus, .form-group textarea:focus {
                    outline: none;
                    border-color: #f59e0b;
                    background: rgba(31, 41, 55, 0.95);
                    box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
                }
                .form-group textarea { resize: vertical; min-height: 80px; }
                .submit-btn {
                    width: 100%;
                    padding: 14px 20px;
                    background: linear-gradient(135deg, #f59e0b 0%, #f97316 100%);
                    color: #111827;
                    border: none;
                    border-radius: 12px;
                    font-weight: 700;
                    font-size: 1rem;
                    cursor: pointer;
                    transition: all 0.3s ease;
                    text-transform: uppercase;
                    letter-spacing: 1px;
                    box-shadow: 0 8px 20px rgba(245, 158, 11, 0.3);
                }
                .submit-btn:hover { transform: translateY(-2px); box-shadow: 0 10px 25px rgba(245, 158, 11, 0.4); }
                .submit-btn:active { transform: translateY(0); }
                .results-section {
                    min-height: 400px;
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    align-items: center;
                    text-align: center;
                }
                .results-section.has-content {
                    justify-content: flex-start;
                    align-items: flex-start;
                    text-align: left;
                }
                .placeholder-text { color: #9ca3af; font-size: 1.1rem; line-height: 1.6; }
                .placeholder-text strong { color: #d1d5db; }
                .recommendation-title {
                    font-size: 1.4rem;
                    margin-bottom: 20px;
                    color: #fbbf24;
                    font-weight: 600;
                }
                .recommendation-content {
                    white-space: pre-wrap;
                    line-height: 1.8;
                    font-size: 0.95rem;
                    color: #e5e7eb;
                }
                .loading { display: none; text-align: center; padding: 40px 20px; }
                .loading.active { display: block; }
                .spinner {
                    border: 4px solid rgba(245, 158, 11, 0.2);
                    border-top: 4px solid #f59e0b;
                    border-radius: 50%;
                    width: 40px;
                    height: 40px;
                    animation: spin 0.8s linear infinite;
                    margin: 0 auto 20px;
                }
                @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
                .error-msg {
                    background: rgba(239, 68, 68, 0.1);
                    border-left: 4px solid #ef4444;
                    padding: 15px;
                    border-radius: 8px;
                    color: #fca5a5;
                    margin-bottom: 15px;
                }
                @media (max-width: 768px) {
                    .container { grid-template-columns: 1fr; }
                    .header h1 { font-size: 2rem; }
                    .results-section { min-height: auto; }
                }
            </style>
        </head>
        <body>
            <div class=\"header\">
                <h1>🔥 FireSmartTV</h1>
                <p>AI-powered recommendations for what to watch</p>
            </div>
            <div class=\"container\">
                <div class=\"form-section\">
                    <h2>What are you in the mood for?</h2>
                    <form id=\"recommendForm\">
                        <div class=\"form-group\">
                            <label for=\"mood\">Mood</label>
                            <select id=\"mood\" name=\"mood\" required>
                                <option value=\"funny\">😄 Funny & lighthearted</option>
                                <option value=\"relaxing\">😌 Relaxing & calming</option>
                                <option value=\"exciting\">⚡ Exciting & thrilling</option>
                                <option value=\"family-friendly\">👨‍👩‍👧‍👦 Family-friendly</option>
                                <option value=\"thought-provoking\">🧠 Thought-provoking</option>
                            </select>
                        </div>

                        <div class=\"form-group\">
                            <label for=\"audience\">Audience</label>
                            <select id=\"audience\" name=\"audience\" required>
                                <option value=\"solo\">Solo viewing</option>
                                <option value=\"couple\">With a partner</option>
                                <option value=\"family\">With family</option>
                                <option value=\"kids\">Kids only</option>
                                <option value=\"friends\">With friends</option>
                            </select>
                        </div>

                        <div class=\"form-group\">
                            <label for=\"time\">Time of day</label>
                            <select id=\"time\" name=\"time\" required>
                                <option value=\"morning\">🌅 Morning</option>
                                <option value=\"afternoon\">☀️ Afternoon</option>
                                <option value=\"evening\">🌆 Evening</option>
                                <option value=\"late-night\">🌙 Late night</option>
                                <option value=\"weekend\">📺 Weekend</option>
                            </select>
                        </div>

                        <div class=\"form-group\">
                            <label for=\"genre\">Favorite genre</label>
                            <select id=\"genre\" name=\"genre\" required>
                                <option value=\"comedy\">Comedy</option>
                                <option value=\"sci-fi\">Sci-Fi</option>
                                <option value=\"thriller\">Thriller</option>
                                <option value=\"drama\">Drama</option>
                                <option value=\"documentary\">Documentary</option>
                                <option value=\"fantasy\">Fantasy</option>
                                <option value=\"romance\">Romance</option>
                            </select>
                        </div>

                        <div class=\"form-group\">
                            <label for=\"extra\">Extra preferences (optional)</label>
                            <textarea id=\"extra\" name=\"extra\" placeholder=\"E.g., 'Something educational but fun', 'Less violence', 'Must have great cinematography'\"></textarea>
                        </div>

                        <button type=\"submit\" class=\"submit-btn\">Get Recommendations</button>
                    </form>
                </div>

                <div class=\"results-section\" id=\"resultsSection\">
                    <div class=\"placeholder-text\">
                        <p><strong>Your AI-powered recommendations will appear here</strong></p>
                        <p style=\"margin-top: 10px; font-size: 0.9rem;\">Fill out the form and click \"Get Recommendations\" to discover what to watch next.</p>
                    </div>
                    <div class=\"loading\" id=\"loading\">
                        <div class=\"spinner\"></div>
                        <p>Finding your perfect show...</p>
                    </div>
                </div>
            </div>

            <script>
                document.getElementById('recommendForm').addEventListener('submit', async function(e) {
                    e.preventDefault();
                    const formData = new FormData(this);
                    const resultsSection = document.getElementById('resultsSection');
                    const loading = document.getElementById('loading');

                    resultsSection.classList.remove('has-content');
                    resultsSection.innerHTML = '';
                    loading.classList.add('active');

                    try {
                        const response = await fetch('/recommend', {
                            method: 'POST',
                            body: formData
                        });

                        const data = await response.json();
                        if (data.success) {
                            resultsSection.classList.add('has-content');
                            resultsSection.innerHTML = `
                                <div class=\"recommendation-title\">✨ Your Recommendations</div>
                                <div class=\"recommendation-content\">${'$'}{escapeHtml(data.result)}</div>
                            `;
                        } else {
                            resultsSection.classList.add('has-content');
                            resultsSection.innerHTML = `
                                <div class=\"error-msg\">
                                    <strong>Oops!</strong> ${'$'}{escapeHtml(data.error || 'Could not generate recommendations. Please try again.')}
                                </div>
                            `;
                        }
                    } catch (error) {
                        resultsSection.classList.add('has-content');
                        resultsSection.innerHTML = `
                            <div class=\"error-msg\">
                                <strong>Error:</strong> ${'$'}{escapeHtml(error.message)}
                            </div>
                        `;
                    } finally {
                        loading.classList.remove('active');
                    }
                });

                function escapeHtml(text) {
                    const div = document.createElement('div');
                    div.textContent = text;
                    return div.innerHTML;
                }
            </script>
        </body>
        </html>
        """;

    private final BedrockService bedrockService;

    public RecommendationController(BedrockService bedrockService) {
        this.bedrockService = bedrockService;
    }

    @GetMapping("/")
    public String index() {
        return HOME_PAGE;
    }

    @PostMapping("/recommend")
    public ResponseEntity<Map<String, Object>> recommend(@RequestParam Map<String, String> formData) {
        String mood = formData.getOrDefault("mood", "funny");
        String audience = formData.getOrDefault("audience", "solo");
        String time = formData.getOrDefault("time", "evening");
        String genre = formData.getOrDefault("genre", "comedy");
        String extra = formData.getOrDefault("extra", "");

        String result = bedrockService.getRecommendations(mood, audience, time, genre, extra);

        Map<String, Object> response = new HashMap<>();
        if (result != null && (result.contains("AWS") || result.contains("Error") || result.contains("error"))) {
            response.put("success", false);
            response.put("error", result);
            return ResponseEntity.ok(response);
        }

        response.put("success", true);
        response.put("result", result);
        return ResponseEntity.ok(response);
    }
}
