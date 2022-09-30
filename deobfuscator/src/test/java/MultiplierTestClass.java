class MultiplierTestClass
{
    private static int dummy(Object... args)
    {
        return 0;
    }

    private int field1051 = -1611704481;
    private int field2701;
    private int field2138, field2130;

    public void test()
    {
        MultiplierTestClass tc = new MultiplierTestClass(); // to trick executor to call the constructor
        int var = 42;

        if (-1 != this.field1051 * 1928543073)
        {
            dummy(this.field1051 * 1928543073);
            this.field1051 = dummy() * 1611704481;
        }

        if (field2701 * 1550405721 > 30000)
        {
            field2701 += -1868498967 * var;
        }

        field2138 = tc.dummy() * 1510226873;
        field2130 = 572701809 * tc.field2138;
        if (-1722291303 * field2130 >= var)
        {
            var = field2130 * -1722291303;
        }
    }
}
