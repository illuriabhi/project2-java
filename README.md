# 📄 Document Similarity Tool (Java, Apache Licensed)

A Java-based tool that compares two text documents and calculates a **similarity score** using **cosine similarity**. It also:

- Tokenizes and cleans the input files
- Removes stopwords and punctuation
- Highlights matching words
- Displays the similarity percentage
- Exports the results to `.txt` and `.pdf`

---

## 🚀 Features

- ✅ Tokenization using Apache OpenNLP
- ✅ Cosine Similarity using Apache Commons Text
- ✅ Export to `.txt` and `.pdf` using Apache PDFBox
- ✅ Stopword removal with customizable list
- ✅ 100% Java and open-source (Apache License 2.0)

---

## 📦 Tech Stack

| Component       | Library                  |
|----------------|---------------------------|
| Tokenization    | Apache OpenNLP           |
| Similarity      | Apache Commons Text      |
| PDF Export      | Apache PDFBox            |
| Build Tool      | Maven                    |

---

## 📁 Project Structure

├── src/
│ └── main/
│ └── java/
│ └── DocumentSimilarityTool.java
├── doc1.txt
├── doc2.txt
├── stopwords.txt
├── similarity_results.txt # (generated)
├── similarity_report.pdf # (generated)
├── pom.xml
└── README.md

## Run
mvn compile
mvn exec:java -Dexec.mainClass="DocumentSimilarityTool"
