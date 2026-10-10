# FireSmartTV

FireSmartTV is an AI-powered Fire TV companion that helps viewers find the right content based on mood, family preferences, time of day, and streaming interests. It uses Amazon Bedrock to generate personalized recommendations and make TV browsing faster and more enjoyable.

## Project goal

FireSmartTV reduces content overload by turning a large streaming catalog into a guided, personalized discovery experience. Instead of manually browsing through endless options, a viewer can express their mood, audience, and time-of-day context, and the app recommends content that fits the moment.

## Why this matters
Many modern TV interfaces overwhelm users with endless choices. FireSmartTV solves this by turning a broad content catalog into a guided, personalized viewing experience using AI.

## What it does
- Understands user mood and preferences
- Recommends movies, shows, or apps for the current time and audience
- Suggests content for family-friendly viewing or solo binge sessions
- Uses AWS Bedrock for intelligent, contextual recommendations

## Architecture
- Frontend: simple Python Flask web app
- AI layer: Amazon Bedrock Runtime
- Data: local sample catalog and user preference inputs
- Deployment: ready for local dev and cloud deployment

## AWS Builder integration
This project uses Amazon Bedrock in the recommendation flow:

1. The user enters preferences like mood, audience, and time.
2. The app sends a prompt to a Bedrock model.
3. The model returns personalized recommendations.
4. The web UI displays the suggestions in a TV-friendly format.

This satisfies the AWS Builder requirement by using a documented AWS AI service in the project flow.

## Project structure

```text
.
├── app.py
├── requirements.txt
├── .env.example
├── .gitignore
├── README.md
└── templates/
    └── index.html
```

## Prerequisites
- Python 3.10+
- AWS account with Bedrock access
- AWS credentials configured locally or via environment variables

## Local setup

1. Clone the repository
2. Create a virtual environment
3. Install dependencies
4. Configure AWS credentials
5. Run the app

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
cp .env.example .env
python app.py
```

Then open:

```text
http://localhost:5000
```

## Environment configuration

Copy `.env.example` into `.env` and update the values:

```env
AWS_REGION=us-east-1
MODEL_ID=anthropic.claude-3-5-sonnet-20240620-v1:0
```

## Example usage
The app asks for:
- Mood (funny, relaxing, action-packed, family-friendly)
- Audience (kids, adults, couples, family)
- Time (morning, evening, weekend)
- Genre interest (comedy, sci-fi, thriller, documentary)

It then returns a personalized Fire TV suggestion set.

## Open-source and hackathon note
This project is open-source and licensed under the MIT License.

## Hackathon-ready summary
"FireSmartTV is an AI-powered TV companion that improves content discovery on Fire TV using Amazon Bedrock. It turns simple user preferences into tailored streaming recommendations, helping people find what to watch faster and with less friction. This matters because smart home and streaming experiences become more personalized, accessible, and engaging when AI drives content discovery."
