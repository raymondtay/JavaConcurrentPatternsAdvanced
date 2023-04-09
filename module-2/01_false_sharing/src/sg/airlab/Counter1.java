package sg.airlab;

import jdk.internal.vm.annotation.*;

public class Counter1 {
  @jdk.internal.vm.annotation.Contended
  public volatile long count1 = 0;
  public volatile long count2 = 0;

}
