package ro.uaic.info.test;

import ro.uaic.info.*;

public class SampleClass {

    @Test
    public void testNoArg() {
        System.out.println("  => SampleClass.testNoArg() a fost executata");
    }

    @Execute
    public void executeWithInt(int value) {
        System.out.println("  => SampleClass.executeWithInt(" + value + ") a fost executata");
    }

    @Test
    public void testAnotherNoArg() {
        System.out.println("  => SampleClass.testAnotherNoArg() a fost executata");
    }

    public void run() {
        System.out.println("  => SampleClass.run() a fost executata");
    }

    private void helper() {
    }
}
