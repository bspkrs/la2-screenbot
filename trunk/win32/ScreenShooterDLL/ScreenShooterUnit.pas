unit ScreenShooterUnit;

interface
uses
  SysUtils,
  Windows, Messages, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, ExtCtrls, StdCtrls, Buttons, Menus, jpeg;
type
  BUFFER = array of byte;
  PBUFFER = ^BUFFER;

  function ScreenShot(hWnd: HWND; width: integer; height: integer; buf: BUFFER; bufferSize: integer ):integer; stdcall; export;

implementation

function ScreenShot(hWnd: HWND; width: integer; height: integer; buf: BUFFER; bufferSize: integer ):integer; stdcall; export;
var
 Image1 : TImage;
 Canvas:TCanvas;
 ScreenV:HDC;
 stream : TMemoryStream;
//  buf : BUFFER;
//  i : integer;
begin
 { buf is a byte buffer where we need to put screenshot }
 { result is the length of data in that buffer }
 { if anything goes wring - return -1 }
 { no need to clear buffer before filling it up }

// buf := buf_;

ScreenV := GetDC(hWnd);

Canvas:=TCanvas.Create();
Canvas.Handle:=ScreenV;


Image1 := TImage.Create(nil);
Image1.Width := width;
Image1.Height := height;

Image1.Canvas.Copyrect(
    Rect(0,0,width,height),
    Canvas,
    Rect(0,0,width,width)
  );
ReleaseDC(hWnd,ScreenV);
Canvas.Free;

Image1.Picture.Bitmap.SaveToFile('screenshot.bmp');

Image1.Free;

end;

end.
