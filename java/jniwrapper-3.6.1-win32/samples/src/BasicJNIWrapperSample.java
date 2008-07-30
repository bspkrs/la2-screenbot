import com.jniwrapper.DefaultLibraryLoader;
import com.jniwrapper.Library;
import com.jniwrapper.Function;
import com.jniwrapper.PlatformContext;

/**
 * This class loads up the native DLL.
 *
 * @author Alexey Razoryonov
 */
public class BasicJNIWrapperSample
{
    protected static Library SAMPLE_LIB;
    static
    {
        String libName = "JNIWrapper";
        if (PlatformContext.isLinux() && PlatformContext.isPPC())
        {
            libName += "_ppc";
        }
        if (PlatformContext.isSunOS())
        {
            libName += "_sunos";
        }
        if (PlatformContext.isX64() || PlatformContext.isPPC64())
        {
            libName += "64";
        }
        SAMPLE_LIB = new Library(libName + "SampleDLL",
                Function.STDCALL_CALLING_CONVENTION);
    }

    static
    {
        DefaultLibraryLoader.getInstance().addPath("../samples/bin");
        SAMPLE_LIB.load();
    }
}
