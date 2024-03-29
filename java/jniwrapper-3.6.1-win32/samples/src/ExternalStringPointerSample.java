import com.jniwrapper.AnsiString;
import com.jniwrapper.ExternalStringPointer;
import com.jniwrapper.Function;
import com.jniwrapper.Pointer;

/**
 * This class demonstrates how to work with {@link com.jniwrapper.ExternalStringPointer}. The native function
 * removeDuplicatesFromString removes duplicated symbols from the specified string.
 *
 * @author Alexey Razoryonov
 */
public class ExternalStringPointerSample extends BasicJNIWrapperSample
{
    public static void main(String[] args)
    {
        Function removeDuplicates = SAMPLE_LIB.getFunction("removeDuplicatesFromString");

        AnsiString str = new AnsiString("Please remove all duplicated symbols from this string.");
        ExternalStringPointer result = new ExternalStringPointer(false);

        removeDuplicates.invoke(null,
                new Pointer(str),
                new Pointer(result));
        System.out.println("The source string: \"" + str + "\"");
        System.out.println("The result string after removing duplicates: \"" + result.readString() + "\"");
    }
}
