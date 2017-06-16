import csv
import json
import sys

def parse_csv(path):
    card_list = []
    with open(path) as csv_file:
        reader = csv.DictReader(csv_file)
        for line in reader:
            card_list.append(line)
    return card_list

def parse_json(card_list):
    for elem in card_list:
        for key in elem.keys():
            translated_str = elem[key].replace('\u201c', '"').replace('\u201c', '"')
            if not (translated_str.startswith(('{','[')) or elem[key] == 'null'):
                translated_str = '"'+translated_str+'"'
            elem[key] = json.loads(translated_str)
    return card_list

def write_json(json_list, path):
    with open(path, 'w') as f:
        dump_str = json.dumps(json_list, indent=4)
        f.write(dump_str)


if __name__ == "__main__":
    try:
        raw_list = parse_csv(sys.argv[1])
        translated_list = parse_json(raw_list)
        write_json(translated_list, sys.argv[2])
    except IndexError:
        print("Invalid number of arguments")
