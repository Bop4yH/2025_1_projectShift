package ru.shift.view.text;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class WrapEditorKit extends StyledEditorKit {

   private static final ViewFactory defaultFactory = new WrapColumnFactory();

   @Override
   public ViewFactory getViewFactory() {
      return defaultFactory;
   }

   private static class WrapColumnFactory implements ViewFactory {

      @Override
      public View create(Element elem) {
         String kind = elem.getName();
         if (kind != null) {
            switch (kind) {
               case AbstractDocument.ContentElementName:
                  return new WrapLabelView(elem);
               case AbstractDocument.ParagraphElementName:
                  return new ParagraphView(elem);
               case AbstractDocument.SectionElementName:
                  return new BoxView(elem, View.Y_AXIS);
               case StyleConstants.ComponentElementName:
                  return new ComponentView(elem);
               case StyleConstants.IconElementName:
                  return new IconView(elem);
            }
         }
         return new LabelView(elem);
      }
   }

   private static class WrapLabelView extends LabelView {

      public WrapLabelView(Element elem) {
         super(elem);
      }

      @Override
      public float getMinimumSpan(int axis) {
         return switch (axis) {
            case View.X_AXIS -> 0;
            case View.Y_AXIS -> super.getMinimumSpan(axis);
            default -> throw new IllegalArgumentException("Invalid axis: " + axis);
         };
      }
   }
}