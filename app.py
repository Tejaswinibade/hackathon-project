import json
import os
from typing import Dict, Any

import boto3
from botocore.exceptions import ClientError
from flask import Flask, render_template_string, request

app = Flask(__name__)

AWS_REGION = os.getenv("AWS_REGION", "us-east-1")
MODEL_ID = os.getenv("MODEL_ID", "anthropic.claude-3-5-sonnet-20240620-v1:0")

bedrock_runtime = boto3.client("bedrock-runtime", region_name=AWS_REGION)

HOME_PAGE = """
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>FireSmartTV</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #0f172a, #111827, #1f2937);
            color: #f3f4f6;
            margin: 0;
            padding: 30px;
        }
        .container {
            max-width: 900px;
            margin: auto;
            background: rgba(17, 24, 39, 0.8);
            border-radius: 18px;
            padding: 30px;
            box-shadow: 0 12px 30px rgba(0,0,0,0.25);
        }
        h1 { color: #fbbf24; }
        form { display: grid; gap: 16px; }
        label { display: block; font-weight: bold; margin-bottom: 6px; }
        input, select, textarea {
            width: 100%;
            padding: 12px;
            border-radius: 10px;
            border: 1px solid #374151;
            background: #111827;
            color: #f9fafb;
            box-sizing: border-box;
        }
        button {
            background: #f59e0b;
            color: #111827;
            border: none;
            border-radius: 10px;
            padding: 14px 18px;
            font-weight: bold;
            cursor: pointer;
        }
        .result {
            margin-top: 20px;
            background: rgba(31, 41, 55, 0.9);
            border-radius: 10px;
            padding: 18px;
            white-space: pre-wrap;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>FireSmartTV</h1>
        <p>AI-powered viewing recommendations for your Fire TV experience.</p>
        <form method="post" action="/recommend">
            <div>
                <label for="mood">What mood are you in?</label>
                <select id="mood" name="mood">
                    <option value="funny">Funny</option>
                    <option value="relaxing">Relaxing</option>
                    <option value="exciting">Exciting</option>
                    <option value="family-friendly">Family-friendly</option>
                </select>
            </div>

            <div>
                <label for="audience">Audience</label>
                <select id="audience" name="audience">
                    <option value="solo">Solo</option>
                    <option value="couple">Couple</option>
                    <option value="family">Family</option>
                    <option value="kids">Kids</option>
                </select>
            </div>

            <div>
                <label for="time">Time of day</label>
                <select id="time" name="time">
                    <option value="evening">Evening</option>
                    <option value="weekend">Weekend</option>
                    <option value="morning">Morning</option>
                    <option value="late-night">Late night</option>
                </select>
            </div>

            <div>
                <label for="genre">Favorite genre</label>
                <select id="genre" name="genre">
                    <option value="comedy">Comedy</option>
                    <option value="sci-fi">Sci-Fi</option>
                    <option value="thriller">Thriller</option>
                    <option value="documentary">Documentary</option>
                </select>
            </div>

            <div>
                <label for="extra">Anything else?</label>
                <textarea id="extra" name="extra" rows="4" placeholder="I want something uplifting and easy to watch with my family."></textarea>
            </div>

            <button type="submit">Get my suggestions</button>
        </form>

        {% if result %}
        <div class="result">{{ result }}</div>
        {% endif %}
    </div>
</body>
</html>
"""


def build_prompt(mood: str, audience: str, time: str, genre: str, extra: str) -> str:
    return (
        "You are a smart TV assistant for a Fire TV app. "
        "Generate 5 personalized viewing recommendations for a user. "
        f"Mood: {mood}. Audience: {audience}. Time of day: {time}. Genre: {genre}. "
        f"Additional note: {extra or 'No extra notes.'}. "
        "Return concise but useful recommendations with title, why it fits, and ideal watch window."
    )


def invoke_bedrock(prompt: str) -> str:
    payload = {
        "anthropic_version": "bedrock-2023-05-31",
        "max_tokens": 400,
        "messages": [
            {
                "role": "user",
                "content": [
                    {"type": "text", "text": prompt}
                ],
            }
        ],
    }

    try:
        response = bedrock_runtime.invoke_model(
            modelId=MODEL_ID,
            body=json.dumps(payload),
            contentType="application/json",
            accept="application/json",
        )

        response_body = json.loads(response["body"].read())
        if "content" in response_body:
            return response_body["content"][0]["text"]
        return "No recommendation available from the model."
    except ClientError as exc:
        error_message = exc.response.get("Error", {}).get("Message", str(exc))
        return f"AWS Bedrock error: {error_message}"


@app.get("/")
def index():
    return render_template_string(HOME_PAGE, result=None)


@app.post("/recommend")
def recommend():
    mood = request.form.get("mood", "funny")
    audience = request.form.get("audience", "family")
    time = request.form.get("time", "evening")
    genre = request.form.get("genre", "comedy")
    extra = request.form.get("extra", "")

    prompt = build_prompt(mood, audience, time, genre, extra)
    result = invoke_bedrock(prompt)
    return render_template_string(HOME_PAGE, result=result)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
