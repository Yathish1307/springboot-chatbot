# University Knowledge Chatbot

A Spring Boot-based WhatsApp chatbot that answers questions from Google Drive documents using AI summarization.

## Features
- WhatsApp integration via Gupshup
- Google Drive document parsing (PDF, DOCX, PPTX, XLSX)
- AI-powered answer summarization using OpenAI GPT
- Web UI for easy access

## Tech Stack
- Spring Boot 3.5.3
- Google Drive API
- Apache Tika (Document parsing)
- OpenAI GPT API
- Gupshup WhatsApp API

## Setup
1. Configure Google Drive service account
2. Set OpenAI API key
3. Configure Gupshup credentials
4. Deploy to cloud platform

## Environment Variables
- `openai.api.key`: Your OpenAI API key
- Google Drive service account JSON file 