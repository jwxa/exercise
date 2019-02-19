package com.github.jwxa;

import com.google.common.io.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Exercise1 {

  private static final String[] LOWERCASE = new String[]{
      "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w"
      , "x", "y", "z"};

  public static void main(String[] args) throws IOException {
    long start = System.currentTimeMillis();
    StringBuffer nameSb = new StringBuffer();
    Random random = new Random();
    IntStream.range(0, 10000000).parallel().forEach((i) -> {
      StringBuilder stringBuilder = new StringBuilder();
      IntStream.range(0, 4).forEach(
          (e) -> {
            String nameUnit = LOWERCASE[random.nextInt(26)];
            stringBuilder.append(nameUnit);
          }
      );
      int baseSalary = random.nextInt(100);
      int bonus = random.nextInt(5);
      stringBuilder.append(",").append(baseSalary).append(",").append(bonus).append("\n");
      nameSb.append(stringBuilder.toString());
    });

    File file = new File("C:\\test.txt");
    FileOutputStream fos = new FileOutputStream(file, true);
    FileChannel fosChannel = fos.getChannel();
    try {
      fosChannel.write(ByteBuffer.wrap(nameSb.toString().getBytes()));
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    try {
      fosChannel.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    try {
      fosChannel.close();
      fos.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    System.out.println("use ms:" + (System.currentTimeMillis() - start));

    List<String> stringList = Files.readLines(file, Charset.defaultCharset());
    List<Salary> salaryList = stringList.parallelStream().map((str) -> {
      String[] split = str.split(",");
      int baseSalary = Integer.parseInt(split[1]);
      int bonus = Integer.parseInt(split[2]);
      Salary salary = new Salary(split[0], baseSalary, bonus, baseSalary * 13 + bonus);
      return salary;
    }).collect(Collectors.toList());

    Map<String, Summary> collectMap = new ConcurrentHashMap<>();
    salaryList.parallelStream().
        forEach((salary -> {
          String prefix = salary.getName().substring(0, 2);
          Summary summary = null;
          if (collectMap.containsKey(prefix)) {
            summary = collectMap.get(prefix);
            summary.addYearSalary(salary.getYearSalary());
            summary.addNum();
          } else {
            summary = new Summary(prefix, salary.getYearSalary(), 1);
            collectMap.put(prefix, summary);
          }
        }));

    List<Map.Entry<String, Summary>> result = collectMap.entrySet()
        .parallelStream()
        .sorted(Comparator.comparingInt(entry -> entry.getValue().getYearSalary()))
        .limit(10)
        .collect(Collectors.toList());
    System.out.println(result);
    System.out.println("total use ms:" + (System.currentTimeMillis() - start));

  }


}

class Summary {
  private String name;

  private int yearSalary;

  private int num;

  public void addYearSalary(int number) {
    this.yearSalary = this.yearSalary + number;
  }

  public void addNum() {
    this.num++;
  }

  public Summary(String name, int yearSalary, int num) {
    this.name = name;
    this.yearSalary = yearSalary;
    this.num = num;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getYearSalary() {
    return yearSalary;
  }

  public void setYearSalary(int yearSalary) {
    this.yearSalary = yearSalary;
  }

  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  @Override
  public String toString() {
    return "Summary{" +
        "name='" + name + '\'' +
        ", yearSalary=" + yearSalary +
        ", num=" + num +
        '}';
  }
}

class Salary {

  private String name;

  private int baseSalary;

  private int bonus;

  private int yearSalary;


  public Salary(String name, int baseSalary, int bonus, int yearSalary) {
    this.name = name;
    this.baseSalary = baseSalary;
    this.bonus = bonus;
    this.yearSalary = yearSalary;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getBaseSalary() {
    return baseSalary;
  }

  public void setBaseSalary(int baseSalary) {
    this.baseSalary = baseSalary;
  }

  public int getBonus() {
    return bonus;
  }

  public void setBonus(int bonus) {
    this.bonus = bonus;
  }

  public int getYearSalary() {
    return yearSalary;
  }

  public void setYearSalary(int yearSalary) {
    this.yearSalary = yearSalary;
  }
}