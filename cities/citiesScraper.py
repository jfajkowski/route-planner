import requests
import lxml.html as lh
import pandas as pd

url = "http://www.polskawliczbach.pl/Miasta"
file_name = "polish_cities.csv"
file_coding = "utf-8"
cities_column = "Miasto"

page = requests.get(url)
doc = lh.fromstring(page.content)
table_rows = doc.xpath('//tr')

scrappedContent = []

for header_elem in table_rows[0]:
    scrappedContent.append((header_elem.text_content(), []))

for i in range(1, len(table_rows)):
    row = table_rows[i]
    j=0
    for item in row.iterchildren():
        scrappedContent[j][1].append(item.text_content())
        j+=1

scrappedContent = {title:column for (title, column) in scrappedContent}
df = pd.DataFrame(scrappedContent[cities_column])
df.to_csv(file_name, sep='|', encoding=file_coding, index=False, header=False, index_label=False)