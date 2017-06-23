import csv
import json
import sys

def isInt(number):
    try:
        int(number)
        return True
    except ValueError:
        return False

def parse_csv(path):
    card_list = []
    with open(path) as csv_file:
        reader = csv.DictReader(csv_file)
        for line in reader:
            card_list.append(line)
    return card_list

def parse_json(card_list):
    i = 2
    for elem in card_list:
        for key in list(elem.keys()):
            if(elem[key] == ''):
                elem.pop(key, None)
                continue
            translated_str = elem[key].replace('\u201c', '"').replace('\u201d', '"').replace('\u2018', "'").replace('\u2019',"'")
            if not (translated_str.startswith(('{','[')) or elem[key] == 'null' or isInt(elem[key])):
                translated_str = '"'+translated_str+'"'
            try:
                elem[key] = json.loads(translated_str)
            except json.decoder.JSONDecodeError:
                print("Parsing Error on line {}, cell {}".format(i, key))
                print("Cell content: {}".format(elem[key]))
                break
        i += 1
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
