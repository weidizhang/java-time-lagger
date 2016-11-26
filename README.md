# java-time-lagger

Created by Weidi Zhang

## Description
Set back time temporarily to run another program. Actual date is then automatically 
adjusted appropriately based on time passed (delay).

## Usage

Must be run as root or administrator.


Help:
```
java -jar timelagger.jar -h
```


Example:
```
java -jar timelagger.jar -d 15 -p "notepad.exe" -t "11-11-11"
```

## License

Please read LICENSE.md to learn about what you can and cannot do with this source code.