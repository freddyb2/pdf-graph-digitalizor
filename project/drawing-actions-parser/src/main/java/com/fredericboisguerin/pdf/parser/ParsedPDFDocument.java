package com.fredericboisguerin.pdf.parser;

import com.fredericboisguerin.pdf.parser.model.DrawingAction;

import java.util.List;

public class ParsedPDFDocument {
    private final DrawingActionsWithImage drawingActionsWithImage;
    private final BorderPoints borderPoints;

    ParsedPDFDocument(DrawingActionsWithImage drawingActionsWithImage, BorderPoints borderPoints) {
        this.drawingActionsWithImage = drawingActionsWithImage;
        this.borderPoints = borderPoints;
    }

    public BorderPoints getBorderPoints() {
        return borderPoints;
    }

    public List<DrawingAction> getDrawingActionsIn(BorderPoints borderPoints) {
        return drawingActionsWithImage.getActionsIn(borderPoints);
    }
}
